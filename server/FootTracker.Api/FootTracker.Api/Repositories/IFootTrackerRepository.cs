using FootTracker.Api.Domain.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FootTracker.Api.Repositories
{
    public interface IFootTrackerRepository<T> where T : BaseModel
    {
        List<T> GetAll();

        T GetById(int id);

        Task<T> Insert(T model);

        Task<T> UpdateGame(T model);

        Task Delete(int id);
    }
}
