package com.github.libliboom.epubviewer.app.mvi

interface StoreFactory {

  fun <Action, State, Result : Any> create(
    name: String? = null,
    initialState: State,
    bootstrapper: Bootstrapper<Action>? = null,
    middlewareFactory: () -> Middleware<Action, State, Result>,
    reducer: Reducer<State, Result>
  ): Store<Action, State>
}
