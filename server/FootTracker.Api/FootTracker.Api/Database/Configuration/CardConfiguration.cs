using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class CardConfiguration : BaseConfiguration
    {
        public CardConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Card>()
                .ToTable("CARDS")
                .HasKey(card => new { card.PlayerId, card.Moment })
                .HasName("PK_Card_Id");

            _modelBuilder.Entity<Card>()
               .Property(card => card.Type)
               .HasColumnName("card_type")
               .HasConversion<string>()
               .IsRequired(true);

            _modelBuilder.Entity<Card>()
                .HasOne(card => card.Player)
                .WithMany(player => player.Cards)
                .HasForeignKey(card => card.PlayerId)
                .HasConstraintName("FK_Card_Player_Id");

            _modelBuilder.Entity<Card>()
                .Property(card => card.PlayerId)
                .HasColumnName("card_player_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Card>()
                .HasOne(card => card.Game)
                .WithMany(game => game.Cards)
                .HasForeignKey(card => card.GameId)
                .HasConstraintName("FK_Card_Game_Id");

            _modelBuilder.Entity<Card>()
                .Property(card => card.GameId)
                .HasColumnName("card_game_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Card>()
                .Property(card => card.Moment)
                .HasColumnName("moment")
                .HasColumnType("TIME")
                .IsRequired(true);
        }
    }
}
