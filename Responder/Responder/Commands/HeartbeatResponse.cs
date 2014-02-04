namespace Responder.AppConfig
{
    public class HeartbeatResponse : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        public int Status { get; set; }
        #endregion

        #region Constructors
        public HeartbeatResponse()
        {
            Type = CommandType.HeartbeatResponse;
        }
        #endregion
    }
}
