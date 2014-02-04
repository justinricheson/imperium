using Responder.Statistics;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Web.Script.Serialization;
using System.Xml;

namespace Responder.AppConfig
{
    public class CommandExecuter
    {
        #region Private Members
        private readonly string _appIdsPath;
        private AppConfig _appConfig;
        private StatisticsCollector _statisticsCollector;
        private bool _helpEnabled;
        #endregion

        #region Constructors
        public CommandExecuter()
        {
            _appIdsPath = 
                Path.Combine(
                Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                "Applications.xml");
            _appConfig = new AppConfig();
            ParseAppIds();

            _statisticsCollector = new StatisticsCollector();
            _statisticsCollector.Start();
        }
        #endregion

        #region Public
        public ICommand Execute(ICommand command)
        {
            switch (command.Type)
            {
                case CommandType.Heartbeat:
                    Logger.Log("Heartbeat processed");
                    var heartbeatResponse = new HeartbeatResponse();
                    heartbeatResponse.Status = _helpEnabled ? 2 : 1;
                    heartbeatResponse.Length = 1;
                    return heartbeatResponse;
                case CommandType.Reboot:
                    Logger.Log("Reboot command processed");
                    Reboot();
                    break;
                case CommandType.LaunchApplication:
                    Logger.Log("Launch application command processed");
                    LaunchApplication(command as LaunchAppCommand);
                    break;
                case CommandType.QueryStatistics:
                    Logger.Log("Query statistics command processed");
                    var queryStatsResponse = new QueryStatisticsResponse();
                    queryStatsResponse.Json = _statisticsCollector.ToJson();
                    queryStatsResponse.Length = queryStatsResponse.Json.Length;
                    Logger.Log("Query statistics response json: {0}", queryStatsResponse.Json);
                    return queryStatsResponse;
                case CommandType.QueryAppConfig:
                    Logger.Log("Query app configuration command processed");
                    var queryAppConfigResponse = new QueryAppConfigResponse();
                    queryAppConfigResponse.Json = AppConfigToJson();
                    queryAppConfigResponse.Length = queryAppConfigResponse.Json.Length;
                    Logger.Log("Query app response json: {0}", queryAppConfigResponse.Json);
                    return queryAppConfigResponse;
                default:
                    Logger.Log("Unknown command type received");
                    break;
            }

            return null;
        }
        public void Start()
        {
            _statisticsCollector.Start();
        }
        public void Stop()
        {
            _statisticsCollector.Stop();
        }
        public void SetHelp(bool helpEnabled)
        {
            _helpEnabled = helpEnabled;
        }
        #endregion

        #region Private
        private void ParseAppIds()
        {
            var doc = new XmlDocument();
            doc.Load(_appIdsPath);

            var apps = doc.GetElementsByTagName("App");
            foreach(XmlNode node in apps)
            {
                var app = new AppProperties();
                app.Name = node.Attributes[0].Value;
                app.ID = node.Attributes[1].Value;
                app.Command = node.Attributes[2].Value;
                app.Arguments = node.Attributes[3].Value;
                _appConfig.AppProperties.Add(app.ID, app);
            }
        }
        private void Reboot()
        {
            Logger.Log("Rebooting system");
            Process.Start("shutdown.exe", "-r -t 0");
        }
        private void LaunchApplication(LaunchAppCommand command)
        {
            Logger.Log("Launching application. AppID: {0}", command.AppId);

            var app = _appConfig.AppProperties[command.AppId.ToString()];
            Process.Start(app.Command, app.Arguments);
        }
        private string AppConfigToJson()
        {
            var jsonSerializer = new JavaScriptSerializer();
            return jsonSerializer.Serialize(_appConfig);
        }
        #endregion
    }
}
