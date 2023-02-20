package ua.cn.stu.multimodule.glue.catalog.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.catalog.presentation.CatalogFilterRouter
import ua.cn.stu.multimodule.catalog.presentation.CatalogRouter
import ua.cn.stu.multimodule.glue.catalog.AdapterCatalogFilterRouter
import ua.cn.stu.multimodule.glue.catalog.AdapterCatalogRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface RouterModule {

    @Binds
    fun bindCatalogRouter(
        catalogRouter: AdapterCatalogRouter
    ): CatalogRouter

    @Binds
    fun bindCatalogFilterRouter(
        router: AdapterCatalogFilterRouter
    ): CatalogFilterRouter

}