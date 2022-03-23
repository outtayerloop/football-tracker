using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class AttachmentConfiguration : BaseConfiguration
    {
        public AttachmentConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<GameAttachment>()
                .ToTable("ATTACHMENTS")
                .HasKey(attachment => attachment.Id)
                .HasName("PK_Attachment_Id");

            _modelBuilder.Entity<GameAttachment>()
                .Property(attachment => attachment.Id)
                .HasColumnName("attachment_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<GameAttachment>()
                .Property(attachment => attachment.Path)
                .HasColumnName("attachment_path")
                .HasColumnType("VARCHAR(300)")
                .IsRequired(true);

            _modelBuilder.Entity<GameAttachment>()
                .HasIndex(attachment => attachment.Path)
                .IsUnique()
                .HasDatabaseName("UC_Attachment_Path");

            _modelBuilder.Entity<GameAttachment>()
                .HasOne(attachment => attachment.Game)
                .WithMany(game => game.Attachments)
                .HasForeignKey(attachment => attachment.GameId)
                .HasConstraintName("FK_Attachment_Game_Id");

            _modelBuilder.Entity<GameAttachment>()
                .Property(attachment => attachment.GameId)
                .HasColumnName("attachment_game_id")
                .HasColumnType("INT")
                .IsRequired(true);
        }
    }
}
