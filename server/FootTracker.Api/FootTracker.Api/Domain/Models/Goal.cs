using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace FootTracker.Api.Domain.Models
{
    public class Goal : BaseModel
    {

        [NotMapped]
        public override int Id { get; set; }

        public int PlayerId { get; set; }

        public Player Player { get; set; }

        public int GameId { get; set; }

        public Game Game { get; set; }

        public TimeSpan Moment { get; set; } 
    }
}
