using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Responder.AppConfig
{
    // Format
    // Command ID    | Length   | Parameters
    // 1 byte        | 2 bytes  | Specified by Length

    public class CommandEncoder
    {
        #region Public
        public byte[] Encode(ICommand command)
        {
            switch (command.Type)
            {
                case CommandType.HeartbeatResponse:
                    return EncodeHeartbeatResponse(command);
                case CommandType.QueryStatisticsResponse:
                    return EncodeQueryStatisticsResponse(command);
                case CommandType.QueryAppConfigResponse:
                    return EncodeQueryAppConfigResponse(command);
                default:
                    return new byte[0];
            }
        }
        #endregion

        #region Private
        private byte[] EncodeHeartbeatResponse(ICommand command)
        {
            var response = command as HeartbeatResponse;

            var bytes = new List<byte>();
            bytes.Add(0x80); // Heartbeat Response
            bytes.AddRange(
                BitConverter.GetBytes((short)command.Length).Reverse());
            bytes.Add((byte)response.Status);

            return bytes.ToArray();
        }
        private byte[] EncodeQueryStatisticsResponse(ICommand command)
        {
            var response = command as QueryStatisticsResponse;

            var bytes = new List<byte>();
            bytes.Add(0x81); // Query Stats Response
            bytes.AddRange(
                BitConverter.GetBytes((short)command.Length).Reverse());
            bytes.AddRange(
                ASCIIEncoding.ASCII.GetBytes(response.Json));

            return bytes.ToArray();
        }
        private byte[] EncodeQueryAppConfigResponse(ICommand command)
        {
            var response = command as QueryAppConfigResponse;

            var bytes = new List<byte>();
            bytes.Add(0x82); // Query App Config Response
            bytes.AddRange(
                BitConverter.GetBytes((short)command.Length).Reverse());
            bytes.AddRange(
                ASCIIEncoding.ASCII.GetBytes(response.Json));

            return bytes.ToArray();
        }
        #endregion
    }
}
