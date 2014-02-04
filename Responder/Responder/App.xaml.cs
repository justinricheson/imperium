using System.Windows;
using Responder;
using System.Windows.Threading;

namespace Responder
{
    public partial class App : Application
    {
        private void Application_DispatcherUnhandledException_1(object sender, DispatcherUnhandledExceptionEventArgs e)
        {
            Logger.Log(e.ToString());
        }
    }
}
