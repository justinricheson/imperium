namespace Responder.AppConfig
{
    public class QueryAppConfigCommand : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        #endregion

        #region Constructors
        public QueryAppConfigCommand()
        {
            Type = CommandType.QueryAppConfig;
        }
        #endregion
    }
}
