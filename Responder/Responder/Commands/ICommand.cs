namespace Responder.AppConfig
{
    public interface ICommand
    {
        CommandType Type { get; }
        int Length { get; }
    }
}
