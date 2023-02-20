package ua.cn.stu.multimodule.formatters.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.formatters.DateTimeFormatter
import ua.cn.stu.multimodule.formatters.DefaultDateTimeFormatter
import ua.cn.stu.multimodule.formatters.DefaultPriceFormatter
import ua.cn.stu.multimodule.formatters.PriceFormatter

@Module
@InstallIn(SingletonComponent::class)
interface FormattersModule {

    @Binds
    fun bindDateTimeFormatter(
        formatter: DefaultDateTimeFormatter
    ): DateTimeFormatter

    @Binds
    fun bindPriceFormatter(
        formatter: DefaultPriceFormatter
    ): PriceFormatter
}