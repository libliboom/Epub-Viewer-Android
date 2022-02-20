package com.github.libliboom.epubviewer.app.mvi

class BaseStoreFactory : StoreFactory {

  override fun <Action, State, Result : Any> create(
    name: String?,
    initialState: State,
    bootstrapper: Bootstrapper<Action>?,
    middlewareFactory: () -> Middleware<Action, State, Result>,
    reducer: Reducer<State, Result>
  ): Store<Action, State> =
    BaseStore(
      initialState = initialState,
      bootstrapper = bootstrapper,
      middleware = middlewareFactory(),
      reducer = reducer
    )
}
