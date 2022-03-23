using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;
using System;

namespace FootTracker.Api.Database.Configuration
{
    public class GameConfiguration : BaseConfiguration
    {
        public GameConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Game>()
               .ToTable("GAMES")
               .HasKey(game => game.Id)
               .HasName("PK_Player_Id");

            _modelBuilder.Entity<Game>()
                .Property(game => game.Id)
                .HasColumnName("game_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.DateTime)
                .HasColumnName("game_datetime")
                .HasColumnType("DATETIME")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.Address)
                .HasColumnName("place")
                .HasColumnType("VARCHAR(100)")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.IsValidated)
                .HasColumnName("validated")
                .HasColumnType("BOOLEAN")
                .HasDefaultValue(false);

            _modelBuilder.Entity<Game>()
                .Property(game => game.TrackedTeamScore)
                .HasColumnName("tracked_team_score")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.OpponentScore)
                .HasColumnName("opponent_score")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.OutCount)
                .HasColumnName("out_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.PenaltyCount)
                .HasColumnName("penalty_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.FreeKickCount)
                .HasColumnName("free_kick_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.CornerCount)
                .HasColumnName("corner_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.OccasionCount)
                .HasColumnName("occasion_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.FaultCount)
                .HasColumnName("fault_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.StopCount)
                .HasColumnName("stop_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.OffsideCount)
                .HasColumnName("offside_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.OverTime)
                .HasColumnName("overtime")
                .HasColumnType("TIME")
                .HasDefaultValue(TimeSpan.FromSeconds(0));

            _modelBuilder.Entity<Game>()
                .Property(game => game.TrackedTeamPossessionCount)
                .HasColumnName("tracked_team_possession_count")
                .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
               .Property(game => game.OpponentTeamPossessionCount)
               .HasColumnName("opponent_team_possession_count")
               .HasColumnType("INT");

            _modelBuilder.Entity<Game>()
                .Property(game => game.TrackedTeamId)
                .HasColumnName("tracked_team_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.OpponentTeamId)
                .HasColumnName("opponent_team_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.RefereeId)
                .HasColumnName("referee_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .Property(game => game.ChampionshipId)
                .HasColumnName("championship_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Game>()
                .HasOne(game => game.TrackedTeam)
                .WithMany(trackedTeam => trackedTeam.TrackedTeamGames)
                .HasForeignKey(game => game.TrackedTeamId)
                .HasConstraintName("FK_Game_Tracked_Team_Id");

            _modelBuilder.Entity<Game>()
                .HasOne(game => game.OpponentTeam)
                .WithMany(opponentTeam => opponentTeam.OpponentTeamGames)
                .HasForeignKey(game => game.OpponentTeamId)
                .HasConstraintName("FK_Game_Opponent_Team_Id");

            _modelBuilder.Entity<Game>()
               .HasOne(game => game.Championship)
               .WithMany(championship => championship.Games)
               .HasForeignKey(game => game.ChampionshipId)
               .HasConstraintName("FK_Game_Championship_Id");

            _modelBuilder.Entity<Game>()
               .HasOne(game => game.Referee)
               .WithMany(referee => referee.Games)
               .HasForeignKey(game => game.RefereeId)
               .HasConstraintName("FK_Game_Referee_Id");

            _modelBuilder.Entity<Game>()
                .HasIndex(game => new { game.DateTime, game.Address })
                .IsUnique()
                .HasDatabaseName("UC_Game_Occurrence");

            _modelBuilder.Entity<Game>()
               .HasMany(game => game.Attachments)
               .WithOne(attachment => attachment.Game)
               .HasForeignKey(attachment => attachment.GameId)
               .HasConstraintName("FK_Attachment_Game_Id")
               .OnDelete(DeleteBehavior.Cascade);

            _modelBuilder.Entity<Game>()
                .HasMany(game => game.Cards)
                .WithOne(card => card.Game)
                .HasForeignKey(card => card.GameId)
                .HasConstraintName("FK_Card_Game_Id")
                .OnDelete(DeleteBehavior.Cascade);

            _modelBuilder.Entity<Game>()
                .HasMany(game => game.Goals)
                .WithOne(goal => goal.Game)
                .HasForeignKey(goal => goal.GameId)
                .HasConstraintName("FK_Goal_Game_Id")
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
