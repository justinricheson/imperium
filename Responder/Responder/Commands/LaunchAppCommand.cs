using System;
using System.Collections.Generic;

namespace Responder.AppConfig
{
    public class LaunchAppCommand : ICommand
    {
        public CommandType Type { get; private set; }
        public int Length { get; set; }
        public int AppId { get; set; }
        public String Argument { get; set; }

        public LaunchAppCommand()
        {
            Type = CommandType.LaunchApplication;
        }
    }
}
