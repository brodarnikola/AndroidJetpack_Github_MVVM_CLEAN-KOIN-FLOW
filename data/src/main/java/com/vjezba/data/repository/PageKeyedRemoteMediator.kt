package com.vjezba.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.dao.LanguagesRepositoriesRemoteKeyDao
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.database.model.RepositoryDetailsResponseDb
import com.vjezba.data.database.model.RepositoryDetailsResponseRemoteKey
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.domain.model.RepositoryDetailsResponse
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: AppDatabase,
    private val redditApi: GithubRepositoryApi,
    private val subredditName: String,
    private val dbMapper: DbMapper,
    private var startFromFirstPage: Int
) : RemoteMediator<Int, RepositoryDetailsResponseDb>() {
    private val postDao = db.languagesRepositoriesDAO()
    private val remoteKeyDao: LanguagesRepositoriesRemoteKeyDao = db.languagesRepositoriesRemoteKeyDAO()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryDetailsResponseDb>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyByPost(subredditName)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if ( remoteKey != null && remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    if ( remoteKey != null && remoteKey.nextPageKey != null) {
                        remoteKey.nextPageKey
                    }
                    else {
                        ""
                    }
                }
            }

            val data = redditApi.searchGithubRepository(
                subredditName,
                loadKey?.toInt() ?: 0, //loadKey?.toInt() ?: 0,
                50
                /*limit = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }*/
            )

            val items = data.items //.children.map { it.data } //.map { it.data }

            db.withTransaction {
                if (loadType == REFRESH) {
                    postDao.deleteGithubRepositories(subredditName)
                    remoteKeyDao.deleteBySubreddit(subredditName)
                }

                remoteKeyDao.insert(RepositoryDetailsResponseRemoteKey(subredditName, data.total_count.toString()))
                val mapList = dbMapper.mapApiResponseGithubToGithubDb(items)
                postDao.insertAll(mapList)
            }

            startFromFirstPage += 1
            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
