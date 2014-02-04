namespace Responder.AppConfig
{
    public class QueryStatisticsResponse : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        public string Json { get; set; }
        #endregion

        #region Constructors
        public QueryStatisticsResponse()
        {
            Type = CommandType.QueryStatisticsResponse;
        }
        #endregion
    }
}
