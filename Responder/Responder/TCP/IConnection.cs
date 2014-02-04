namespace Responder.TCP
{
    public interface IConnection
    {
        void Connect();
        void Disconnect();
        void Send(byte[] data);

        event ReceiveDataDelegate OnDataReceived;
        event ConnectionStateChangedDelegate OnConnectionStateChanged;
    }
}
