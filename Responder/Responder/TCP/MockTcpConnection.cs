using System.Timers;
using System.Collections.Generic;
using System.Text;
using System;

namespace Responder.TCP
{
    public class MockTcpConnection : IConnection
    {
        #region Private
        private int _receiveCount;
        private Timer _receiveTimer;
        #endregion

        #region Constructors
        public MockTcpConnection()
        {
            _receiveTimer = new Timer();
            _receiveTimer.Interval = 1000;
            _receiveTimer.Elapsed += _receiveTimer_Elapsed;
        }
        #endregion

        #region Public
        public void Connect()
        {
            _receiveTimer.Start();
            OnConnectionStateChanged(this, new ConnectionStateChangedEventArgs(true));
        }
        public void Disconnect()
        {
            _receiveTimer.Stop();
            _receiveCount = 0;
            OnConnectionStateChanged(this, new ConnectionStateChangedEventArgs(false));
        }
        public void Send(byte[] data)
        {
        }

        public event ReceiveDataDelegate OnDataReceived;
        public event ConnectionStateChangedDelegate OnConnectionStateChanged;
        #endregion

        #region Private
        private void NotifyDataReceived(byte[] data)
        {
            if (OnDataReceived != null)
                OnDataReceived(this, new ReceiveDataEventArgs(data));
        }
        private void NotifyConnectionStateChanged(bool isConnected)
        {
            if (OnConnectionStateChanged != null)
                OnConnectionStateChanged(this, new ConnectionStateChangedEventArgs(isConnected));
        }
        #endregion

        #region Event Handlers
        private void _receiveTimer_Elapsed(object sender, ElapsedEventArgs e)
        {
            List<byte> data = null;
            switch (_receiveCount)
            {
                case 1:
                    //data = new List<byte>() { 0x01, 0x00, 0x00 }; // Reboot
                    break;
                case 2:
                    //data = new List<byte>() { 0x02, 0x00, 0x01, 0x00 }; // Launch app
                    break;
                case 10: // Need to wait for stats to be generated
                    //data = new List<byte>() { 0x03, 0x00, 0x00 }; // Query stats
                    break;
                case 11:
                    data = new List<byte>() { 0x04, 0x00, 0x00 }; // Query app config
                    break;
                default:
                    //data = new List<byte>() { 0x00, 0x00, 0x00 }; // Heartbeat
                    break;
            }

            _receiveCount++;
            if (data != null)
                OnDataReceived(this, new ReceiveDataEventArgs(data.ToArray()));
        }
        #endregion
    }
}
