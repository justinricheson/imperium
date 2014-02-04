using Responder.AppConfig;
using Responder.TCP;
using System;
using System.Linq;
using System.Windows;

namespace Responder
{
    public partial class MainWindow : Window
    {
        #region Private Members
        private IConnection _tcpConnection;
        private CommandParser _commandParser;
        private CommandEncoder _commandEncoder;
        private CommandExecuter _commandExecuter;
        private bool _isRunning;
        #endregion

        #region Constructors
        public MainWindow()
        {
            #if DEBUG
            System.Diagnostics.Debugger.Launch();
            #endif

            InitializeComponent();

            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;
            Logger.OnLogData += Logger_OnLogData;

            _commandParser = new CommandParser();
            _commandEncoder = new CommandEncoder();
            _commandExecuter = new CommandExecuter();
        }
        #endregion

        #region Private
        private void Start()
        {
            if (_isRunning) return;

            lb_status.Items.Clear();
            Logger.Log("Service Started");

            _commandExecuter.Start();

            _tcpConnection = ConnectionFactory.GetListener(1234);
            _tcpConnection.OnConnectionStateChanged += _tcpConnection_OnConnectionStateChanged;
            _tcpConnection.OnDataReceived += _tcpConnection_OnDataReceived;
            _tcpConnection.Connect();

            _isRunning = true;
        }
        private void Stop()
        {
            if (!_isRunning) return;

            _commandExecuter.Stop();
            _tcpConnection.Disconnect();
            
            _isRunning = false;
            Logger.Log("Service Stopped");
        }
        #endregion

        #region Event Handlers
        private void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            Logger.Log(e.ToString());
        }
        private void Start_Button_Click(object sender, RoutedEventArgs e)
        {
            Start();
        }
        private void Stop_Button_Click(object sender, RoutedEventArgs e)
        {
            Stop();
        }
        private void Help_Button_Click(object sender, RoutedEventArgs e)
        {
            _commandExecuter.SetHelp(tbHelp.IsChecked.Value);
            Logger.Log("Help {0}", tbHelp.IsChecked.Value ? "enabled" : "disabled");
        }
        private void _tcpConnection_OnDataReceived(object sender, ReceiveDataEventArgs e)
        {
            var bytes = e.Data.ToList();
            Logger.Log("Data received: {0}", bytes);

            var command = _commandParser.Parse(bytes);
            if (command != null)
            {
                var response = _commandExecuter.Execute(command);
                if (response != null)
                {
                    var data = _commandEncoder.Encode(response);
                    _tcpConnection.Send(data);
                    Logger.Log("Data sent: {0}", BitConverter.ToString(data));
                }
            }
        }
        private void _tcpConnection_OnConnectionStateChanged(object sender, ConnectionStateChangedEventArgs e)
        {
            Logger.Log("Tcp IsConnected: {0}", e.IsConnected);

            if (!e.IsConnected)
                _commandParser.FlushQueue();
        }
        private void Logger_OnLogData(object sender, LogDataEventArgs e)
        {
            Dispatcher.Invoke((Action)delegate()
            {
                lb_status.Items.Add(e.Log);
                lb_status.ScrollIntoView(e.Log);
            });
        }
        #endregion
    }
}
