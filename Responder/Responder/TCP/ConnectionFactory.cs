namespace Responder.TCP
{
    public static class ConnectionFactory
    {
        public static IConnection GetListener(int port)
        {
            return new TcpConnection(port);
            //return new MockTcpConnection();
        }
    }
}
