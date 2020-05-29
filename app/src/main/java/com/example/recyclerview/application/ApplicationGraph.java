package com.example.recyclerview.application;

import com.example.recyclerview.presentation.ShowHeadline.ShowHeadlinePresenter;
import com.example.recyclerview.repository.NewsRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Definition of the Application graph
 */
@Singleton
@Component
public interface ApplicationGraph {
    NewsRepository newsRepository();
}