using Microsoft.Win32;
using System;
using System.Management;
using System.Timers;
using System.Web.Script.Serialization;

namespace Responder.Statistics
{
    public class StatisticsCollector
    {
        #region Private Members
        private DateTime _startTime;
        private Timer _collectStatsTimer;
        #endregion

        #region Properties
        public Statistics Statistics { get; private set; }
        #endregion

        #region Constructors
        public StatisticsCollector()
        {
            _startTime = DateTime.Now;
            Statistics = new Statistics();
            _collectStatsTimer = new Timer();
            _collectStatsTimer.Interval = 10000;
            _collectStatsTimer.Elapsed += _collectStatsTimer_Elapsed;

            GetBasicData();
        }
        #endregion

        #region Public
        public void Start()
        {
            _collectStatsTimer.Start();
        }
        public void Stop()
        {
            _collectStatsTimer.Stop();
        }
        public string ToJson()
        {
            var jsonSerializer = new JavaScriptSerializer();
            return jsonSerializer.Serialize(Statistics);
        }
        #endregion

        #region Event Handlers
        private void _collectStatsTimer_Elapsed(object sender, ElapsedEventArgs e)
        {
            try
            {
                Logger.Log("Collecting system statistics");
                GetBasicData();
                GetHardwareInformation();
                GetInstalledApplications();
            }
            catch (Exception ex)
            {
                Logger.Log(ex.ToString());
            }
        }
        #endregion

        #region Private
        private void GetBasicData()
        {
            Statistics.UpTime = (int)(DateTime.Now - _startTime).TotalSeconds;
            Statistics.ComputerName = Environment.MachineName;
            Statistics.UserName = Environment.UserName;
            Statistics.OsVersion = Environment.OSVersion.ToString();
        }
        private void GetHardwareInformation()
        {
            // Computer model and memory information
            var query = new ManagementObjectSearcher("SELECT * FROM Win32_ComputerSystem");
            var results = query.Get();

            foreach (var mo in results)
            {
                Statistics.Model = mo["model"].ToString();
                Statistics.Memory = mo["totalphysicalmemory"].ToString();
            }

            // Processor type and speed
            query = new ManagementObjectSearcher("SELECT * FROM Win32_Processor");
            results = query.Get();

            foreach (ManagementObject mo in results)
            {
                Statistics.Processor = mo["Name"].ToString();
            }

            // Disk drives, total capacity and free space
            query = new ManagementObjectSearcher("SELECT * FROM Win32_LogicalDisk");
            results = query.Get();

            Statistics.Drives.Clear();
            foreach (var mo in results)
            {
                var drive = new DriveStatistics();
                drive.Caption = mo["caption"] == null ? "" : mo["caption"].ToString();
                drive.Description = mo["description"] == null ? "" : mo["description"].ToString();
                drive.FileSystem = mo["filesystem"] == null ? "" : mo["filesystem"].ToString();
                drive.FreeSpace = mo["freespace"] == null ? "" : mo["freespace"].ToString();
                drive.Size = mo["size"] == null ? "" : mo["size"].ToString();
                Statistics.Drives.Add(drive);
            }
        }
        private void GetInstalledApplications()
        {
            Statistics.Applications.Clear();

            RegistryKey regKey, regSubKey;

            regKey = Registry.LocalMachine;
            regKey = regKey.OpenSubKey("SOFTWARE").OpenSubKey("Microsoft").
                            OpenSubKey("Windows").OpenSubKey("CurrentVersion").
                            OpenSubKey("Uninstall");

            string[] software = regKey.GetSubKeyNames();
            for (int i = 0; i < software.Length; i++)
            {
                string application = software[i].ToString();
                regSubKey = regKey.OpenSubKey(software[i]);
                if (regSubKey.GetValue("DisplayName") != null)
                {
                    string appName = regSubKey.GetValue("DisplayName").ToString();
                    if (!appName.Contains("KB") && !appName.Contains("Microsoft") && !appName.Contains("Windows"))
                        Statistics.Applications.Add(appName);
                }
            }
        }
        #endregion
    }
}
