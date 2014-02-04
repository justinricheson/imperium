using System;
using System.Collections.Generic;
using System.Linq;

namespace Responder.AppConfig
{
    // Format
    // Command ID    | Length   | Parameters
    // 1 byte        | 2 bytes  | Specified by Length

    public class CommandParser
    {
        #region Private
        private List<byte> _unparsedData;
        #endregion

        #region Constructors
        public CommandParser()
        {
            _unparsedData = new List<byte>();
        }
        #endregion

        #region Public
        public ICommand Parse(List<byte> bytes)
        {
            if (bytes == null || bytes.Count < 1)
                return null; // No change

            _unparsedData.AddRange(bytes);
            if (_unparsedData.Count < 3)
                return null; // Length field not complete

            switch (_unparsedData[0])
            {
                case 0x00:
                    return ParseHeartbeat();
                case 0x01:
                    return ParseRebootCommand();
                case 0x02:
                    return ParseLaunchAppCommand();
                case 0x03:
                    return ParseQueryStatisticsCommand();
                case 0x04:
                    return ParseQueryAppConfigCommand();
                default: // Invalid command byte
                    FlushQueue();
                    return null;
            }
        }
        public void FlushQueue()
        {
            _unparsedData.Clear();
        }
        #endregion

        #region Private
        private ICommand ParseHeartbeat()
        {
            var heartbeat = new Heartbeat();
            heartbeat.Length = GetLength();

            if (_unparsedData.Count < heartbeat.Length + 3)
                return null;
            else
            {
                RemoveMessageFromQueue(heartbeat.Length);
                return heartbeat;
            }
        }
        private ICommand ParseRebootCommand()
        {
            var rebootCommand = new RebootCommand();
            rebootCommand.Length = GetLength();

            if (_unparsedData.Count < rebootCommand.Length + 3)
                return null;
            else
            {
                RemoveMessageFromQueue(rebootCommand.Length);
                return rebootCommand;
            }
        }
        private ICommand ParseLaunchAppCommand()
        {
            var launchAppCommand = new LaunchAppCommand();
            launchAppCommand.Length = GetLength();

            if (_unparsedData.Count < launchAppCommand.Length + 3)
                return null;
            else
            {
                var data = _unparsedData.GetRange(0 + 3, launchAppCommand.Length);
                launchAppCommand.AppId = data[0];

                RemoveMessageFromQueue(launchAppCommand.Length);
                return launchAppCommand;
            }
        }
        private ICommand ParseQueryStatisticsCommand()
        {
            var queryStatsCommand = new QueryStatisticsCommand();
            queryStatsCommand.Length = GetLength();

            if (_unparsedData.Count < queryStatsCommand.Length + 3)
                return null;
            else
            {
                RemoveMessageFromQueue(queryStatsCommand.Length);
                return queryStatsCommand;
            }
        }
        private ICommand ParseQueryAppConfigCommand()
        {
            var queryAppConfigCommand = new QueryAppConfigCommand();
            queryAppConfigCommand.Length = GetLength();

            if (_unparsedData.Count < queryAppConfigCommand.Length + 3)
                return null;
            else
            {
                RemoveMessageFromQueue(queryAppConfigCommand.Length);
                return queryAppConfigCommand;
            }
        }
        private void RemoveMessageFromQueue(int length)
        {
            _unparsedData.RemoveRange(0, length + 3);
        }
        private int GetLength()
        {
            var byte1 = Convert.ToInt32(_unparsedData[1]);
            var byte2 = Convert.ToInt32(_unparsedData[2]);

            return (byte1 * 256) + byte2;
        }
        #endregion
    }
}
