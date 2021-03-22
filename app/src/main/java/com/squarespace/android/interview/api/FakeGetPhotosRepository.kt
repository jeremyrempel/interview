package com.squarespace.android.interview.api

import android.os.Handler
import android.os.Looper
import com.squarespace.android.interview.Photo
import java.util.concurrent.atomic.AtomicBoolean

class FakeGetPhotosRepository {

    /**
     * Query results on a background process and push back to main thread via callback
     *
     * @param callback with either success or failure result
     * @return cancellable job
     */
    fun getPhotosList(callback: (Result<List<Photo>>) -> Unit): () -> Unit {
        val cancelled = AtomicBoolean(false)
        val cancelJob = {
            cancelled.getAndSet(true)
            Unit
        }

        val handler = Handler(Looper.getMainLooper())
        Runnable {
            if (cancelled.get()) return@Runnable
            Thread.sleep(3000)

            if (cancelled.get()) return@Runnable

            val data = fakePhotoRepo + fakePhotoRepo + fakePhotoRepo + fakePhotoRepo + fakePhotoRepo

            handler.post {
                callback(Result.Success(data))
            }

        }.run()

        return cancelJob
    }

    /**
     * Get photos by id async with an artificial delay
     *
     * @param async callback with result or error
     * @return job to cancel task
     */
    fun getPhotoById(id: Int, callback: (Result<Photo>) -> Unit): () -> Unit {
        val cancelled = AtomicBoolean(false)
        val cancelJob = {
            cancelled.getAndSet(true)
            Unit
        }

        val handler = Handler(Looper.getMainLooper())
        Runnable {
            if (cancelled.get()) return@Runnable
            Thread.sleep(20)

            if (cancelled.get()) return@Runnable

            val data = fakePhotoRepo.find { it.id == id }
            handler.post {
                if (data != null) {
                    callback(Result.Success(data))
                } else {
                    callback(Result.Failure(IllegalArgumentException("photo id not found")))
                }
            }
        }.run()

        return cancelJob
    }
}

sealed class Result<T> {
    class Success<T>(val successResult: T) : Result<T>()
    class Failure<T>(val failureResult: Throwable) : Result<T>()
}

val fakePhotoRepo = listOf(
    Photo(
        1,
        "person in gray sweater holding Surface device",
        "https://images.unsplash.com/photo-1612831660846-778e3a5548bd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MXwxfGFsbHwxfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80&w=200",
        "https://images.unsplash.com/photo-1612831660846-778e3a5548bd?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyMTY5NDd8MXwxfGFsbHwxfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=85"
    ),
    Photo(
        2,
        "\"#stopasianhate, Donate at gofundme.com/aapi",
        "https://images.unsplash.com/photo-1616337793116-5eb0d33c910d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHwyfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80&w=200",
        "https://images.unsplash.com/photo-1616337793116-5eb0d33c910d?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHwyfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=85"
    ),
    Photo(
        3,
        "low angle photography of high rise building",
        "https://images.unsplash.com/photo-1616338968156-76ff58fa9197?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHwzfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80&w=200",
        "https://images.unsplash.com/photo-1616338968156-76ff58fa9197?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHwzfHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=85"
    ),
    Photo(
        4,
        "person with braided hair wearing black shirt",
        "https://images.unsplash.com/photo-1616258802130-5177dd65cc33?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHw0fHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80&w=200",
        "https://images.unsplash.com/photo-1616258802130-5177dd65cc33?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHw0fHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80"
    ),
    Photo(
        5,
        "white and gray floral throw pillows on brown wooden table",
        "https://images.unsplash.com/photo-1616258802130-5177dd65cc33?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHw0fHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=80&w=200",
        "https://images.unsplash.com/photo-1616323780877-35cc3453ae2f?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyMTY5NDd8MHwxfGFsbHw1fHx8fHx8Mnx8MTYxNjQyMjM5NQ&ixlib=rb-1.2.1&q=85"
    )
)