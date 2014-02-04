
namespace Responder.AppConfig
{
    public class Heartbeat : ICommand
    {
        public CommandType Type { get; private set; }
        public int Length { get; set; }

        public Heartbeat()
        {
            Type = CommandType.Heartbeat;
        }
    }
}
