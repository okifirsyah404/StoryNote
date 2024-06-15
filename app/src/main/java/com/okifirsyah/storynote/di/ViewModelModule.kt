package com.okifirsyah.storynote.di

import com.okifirsyah.storynote.presentation.MainActivityViewModel
import com.okifirsyah.storynote.presentation.view.add_story.AddStoryViewModel
import com.okifirsyah.storynote.presentation.view.dashboard.DashboardViewModel
import com.okifirsyah.storynote.presentation.view.detail_story.DetailStoryViewModel
import com.okifirsyah.storynote.presentation.view.maps.StoryMapsViewModel
import com.okifirsyah.storynote.presentation.view.setting.SettingViewModel
import com.okifirsyah.storynote.presentation.view.sign_in.SignInViewModel
import com.okifirsyah.storynote.presentation.view.sign_up.SignUpViewModel
import com.okifirsyah.storynote.presentation.view.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainActivityViewModel(get())
    }

    viewModel {
        SplashViewModel(get())
    }

    viewModel {
        SignUpViewModel(get())
    }

    viewModel {
        SignInViewModel(get())
    }

    viewModel {
        DashboardViewModel(get())
    }

    viewModel {
        SettingViewModel(get())
    }

    viewModel {
        DetailStoryViewModel(get())
    }

    viewModel {
        AddStoryViewModel(get())
    }

    viewModel {
        StoryMapsViewModel(get())
    }

}