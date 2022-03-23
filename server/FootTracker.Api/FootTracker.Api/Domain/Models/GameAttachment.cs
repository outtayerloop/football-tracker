namespace FootTracker.Api.Domain.Models
{
    public class GameAttachment : BaseModel
    {
        public string Path { get; set; }

        public int GameId { get; set; }

        public Game Game { get; set; }
    }
}
