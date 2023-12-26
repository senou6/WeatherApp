package pt.pedro.ccti.weatherapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegularApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegionSearchApi