# GitGlobalHooksLocator

![Build](https://github.com/y0ung3r/GitGlobalHooksLocator/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/com.github.y0ung3r.gitglobalhookslocator)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/com.github.y0ung3r.gitglobalhookslocator)

<!-- Plugin description -->
Intellij plugin to enable or disable global Git hooks

Hooks are programs you can place in a hooks directory (specified in core.hooksPath) to trigger actions at certain points in git’s execution.
Unfortunately, Intellij can only keep track of local hooks located inside a project.
With this plugin, you can enable or disable your global Git hooks directly from the IDE.
The following hooks are available:
- pre-commit
- prepare-commit-msg
- commit-msg
- post-commit
- applypatch-msg
- pre-applypatch
- post-applypatch
- pre-rebase
- post-rewrite
- post-checkout
- post-merge
- pre-push
- pre-auto-gc
<!-- Plugin description end -->

![Git Global Hooks Locator](src/main/resources/description.png)

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GitGlobalHooksLocator"</kbd> >
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/y0ung3r/GitGlobalHooksLocator/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
