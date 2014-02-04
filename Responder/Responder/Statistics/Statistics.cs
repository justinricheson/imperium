using System.Collections.Generic;

namespace Responder.Statistics
{
    public class Statistics
    {
        #region Properties
        public int UpTime { get; set; }
        public string ComputerName { get; set; }
        public string UserName { get; set; }
        public string OsVersion { get; set; }
        public string Model { get; set; }
        public string Memory { get; set; }
        public string Processor { get; set; }
        public List<DriveStatistics> Drives { get; private set; }
        public List<string> Applications { get; private set; }
        #endregion

        #region Constructors
        public Statistics()
        {
            Drives = new List<DriveStatistics>();
            Applications = new List<string>();
        }
        #endregion
    }
}
