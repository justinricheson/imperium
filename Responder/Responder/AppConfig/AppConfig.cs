using System.Collections.Generic;

namespace Responder.AppConfig
{
    public class AppConfig
    {
        #region Properties
        public Dictionary<string, AppProperties> AppProperties { get; set; }
        #endregion

        #region Constructors
        public AppConfig()
        {
            AppProperties = new Dictionary<string, AppProperties>();
        }
        #endregion
    }
}
