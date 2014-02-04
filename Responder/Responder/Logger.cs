using System;
using System.Collections.Generic;
using System.IO;

namespace Responder
{
    public static class Logger
    {
        #region Properties
        public static String LogLocation { get; set; }
        #endregion

        #region Constructors
        static Logger()
        {
            LogLocation = String.Format("C:\\log_{0}.log", GetDateTimeString());
        }
        #endregion

        #region Public
        public static void Log(string logString, List<byte> bytes)
        {
            Log(logString, BitConverter.ToString(bytes.ToArray()));
        }
        public static void Log(string logString, params object[] args)
        {
            try
            {
                string log = String.Format("{0} - {1}.", DateTime.Now,
                             String.Format(logString, args));

                using (var wtr = TextWriter.Synchronized(new StreamWriter(LogLocation, true)))
                    wtr.WriteLine(log);

                NotifyOnLogData(log);
            }
            catch
            {
                // What the hell do you do when the logger fails??
            }
        }

        public static event LogDataDelegate OnLogData;
        #endregion

        #region Private
        private static void NotifyOnLogData(String data)
        {
            if (OnLogData != null)
                OnLogData(null, new LogDataEventArgs(data));
        }
        private static string GetDateTimeString()
        {
            var now = DateTime.Now;
            return String.Format("{0}_{1}_{2}_{3}_{4}_{5}",
                now.Year,
                now.Month,
                now.Day,
                now.Hour,
                now.Minute,
                now.Second);
        }
        #endregion
    }

    public delegate void LogDataDelegate(object sender, LogDataEventArgs e);
    public class LogDataEventArgs
    {
        #region Properties
        public String Log { get; private set; }
        #endregion

        #region Constructors
        public LogDataEventArgs(String log)
        {
            Log = log;
        }
        #endregion
    }
}
