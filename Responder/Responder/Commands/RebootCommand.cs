namespace Responder.AppConfig
{
    public class RebootCommand : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        #endregion

        #region Constructors
        public RebootCommand()
        {
            Type = CommandType.Reboot;
        }
        #endregion
    }
}
