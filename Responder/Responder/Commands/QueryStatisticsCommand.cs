namespace Responder.AppConfig
{
    public class QueryStatisticsCommand : ICommand
    {
        #region Properties
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        #endregion

        #region Constructors
        public QueryStatisticsCommand()
        {
            Type = CommandType.QueryStatistics;
        }
        #endregion
    }
}
