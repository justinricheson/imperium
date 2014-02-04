namespace Responder.AppConfig
{
    public class QueryAppConfigResponse : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        public string Json { get; set; }
        #endregion

        #region Constructors
        public QueryAppConfigResponse()
        {
            Type = CommandType.QueryAppConfigResponse;
        }
        #endregion
    }
}
