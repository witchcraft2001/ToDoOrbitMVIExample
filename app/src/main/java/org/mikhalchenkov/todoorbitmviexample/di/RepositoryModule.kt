package org.mikhalchenkov.todoorbitmviexample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mikhalchenkov.todoorbitmviexample.core.data.repository.TodoRepositoryImpl
import org.mikhalchenkov.todoorbitmviexample.core.domain.repository.TodoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository
}