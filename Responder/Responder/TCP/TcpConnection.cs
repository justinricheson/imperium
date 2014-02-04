using System;
using System.Collections.ObjectModel;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace Responder.TCP
{
    public class TcpConnection : IConnection
    {
        #region Private Members
        private Thread _listenThread;
        private TcpListener _tcpListener;
        private TcpClient _tcpClient;
        private bool _listenForClients;
        #endregion

        #region Constructors
        public TcpConnection(int port)
        {
            _tcpListener = new TcpListener(IPAddress.Any, port);
        }
        #endregion

        #region Public
        public void Connect()
        {
            _listenForClients = true;
            _tcpListener.Start();

            _listenThread = new Thread(ListenForClients);
            _listenThread.Start();
        }
        public void Disconnect()
        {
            _listenForClients = false;
            _tcpListener.Stop();
        }
        public void Send(byte[] data)
        {
            SendData(data);
        }

        public event ReceiveDataDelegate OnDataReceived;
        public event ConnectionStateChangedDelegate OnConnectionStateChanged;
        #endregion

        #region Private
        private void ListenForClients()
        {
            while (_listenForClients)
            {
                try
                {
                    _tcpClient = _tcpListener.AcceptTcpClient();
                    NotifyConnectionStateChanged(true);

                    byte[] data = null;
                    while (ReceiveData(out data))
                    {
                        NotifyDataReceived(data);
                        if (!_listenForClients)
                            break;
                    }
                }
                catch
                {
                }
                finally
                {
                    NotifyConnectionStateChanged(false);
                }
            }
        }
        private bool ReceiveData(out byte[] data)
        {
            if (!_tcpClient.Connected)
            {
                data = new byte[0];
                return false;
            }

            var clientStream = _tcpClient.GetStream();

            byte[] message = new byte[4096];
            int bytesRead = clientStream.Read(message, 0, 4096);
            if (bytesRead == 0)
            {
                data = new byte[0];
                return false;
            }

            // Remove trailing empty data
            byte[] tempData = new byte[bytesRead];
            Array.Copy(message, tempData, bytesRead);

            data = tempData;
            return true;
        }
        private void SendData(byte[] data)
        {
            if (_tcpClient != null && _tcpClient.Connected)
            {
                using (var clientStream = _tcpClient.GetStream())
                    clientStream.Write(data, 0, data.Length);
            }
        }
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
    }

    public delegate void ReceiveDataDelegate(object sender, ReceiveDataEventArgs e);
    public class ReceiveDataEventArgs : EventArgs
    {
        #region Constructors
        public ReceiveDataEventArgs(byte[] data)
        {
            Data = new ReadOnlyCollection<byte>(data);
        }
        #endregion

        #region Public
        public  ReadOnlyCollection<byte> Data { get; private set; }
        #endregion
    }

    public delegate void ConnectionStateChangedDelegate(object sender, ConnectionStateChangedEventArgs e);
    public class ConnectionStateChangedEventArgs : EventArgs
    {
        #region Properties
        public bool IsConnected { get; private set; }
        #endregion

        #region Constructors
        public ConnectionStateChangedEventArgs(bool isConnected)
        {
            IsConnected = isConnected;
        }
        #endregion
    }
}
