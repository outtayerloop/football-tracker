using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class PlayerConfiguration : BaseConfiguration
    {
        public PlayerConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Player>()
               .ToTable("PLAYERS")
               .HasKey(player => player.Id)
               .HasName("PK_Player_Id");

            _modelBuilder.Entity<Player>()
                .Property(player => player.Id)
                .HasColumnName("player_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Player>()
                .Property(player => player.FullName)
                .HasColumnName("player_full_name")
                .HasColumnType("VARCHAR(300)")
                .IsRequired(true);

            _modelBuilder.Entity<Player>()
               .Property(player => player.Position)
               .HasColumnName("position")
               .HasConversion<string>()
               .IsRequired(true);

            _modelBuilder.Entity<Player>()
                .HasMany(player => player.Cards)
                .WithOne(card => card.Player)
                .HasForeignKey(card => card.PlayerId)
                .HasConstraintName("FK_Card_Player_Id")
                .OnDelete(DeleteBehavior.Cascade);

            _modelBuilder.Entity<Player>()
                .HasMany(player => player.Goals)
                .WithOne(goal => goal.Player)
                .HasForeignKey(goal => goal.PlayerId)
                .HasConstraintName("FK_Goal_Player_Id")
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
