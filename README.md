# ViperX

[![GitHub](https://forthebadge.com/badges/gluten-free.svg)](https://github.com/xmoderlive/viperx) [![forthebadge](https://forthebadge.com/badges/built-with-love.svg)](https://github.com/xmoderlive/viperx) [![![NBT-API](data:image/svg+xml;base64,PHN2ZyBkYXRhLXYtODY1ODNhZmQ9IiIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTc5LjExNjY2ODcwMTE3MTg4IiBoZWlnaHQ9IjM1IiB2aWV3Qm94PSIwIDAgMTc5LjExNjY2ODcwMTE3MTg4IDM1IiBjbGFzcz0iYmFkZ2Utc3ZnIj48ZGVmcyBkYXRhLXYtODY1ODNhZmQ9IiI+PCEtLS0tPjwhLS0tLT48IS0tLS0+PC9kZWZzPjxyZWN0IGRhdGEtdi04NjU4M2FmZD0iIiB3aWR0aD0iOTUuMDE2NjcwMjI3MDUwNzgiIGhlaWdodD0iMzUiIGZpbGw9IiMyYTRiOTkiLz48cmVjdCBkYXRhLXYtODY1ODNhZmQ9IiIgeD0iOTUuMDE2NjcwMjI3MDUwNzgiIHdpZHRoPSI4NC4wOTk5OTg0NzQxMjExIiBoZWlnaHQ9IjM1IiBmaWxsPSIjYjhiOWQwIi8+PCEtLS0tPjx0ZXh0IGRhdGEtdi04NjU4M2FmZD0iIiB4PSI0Ny41MDgzMzUxMTM1MjUzOSIgeT0iMTcuNSIgZHk9IjAuMzVlbSIgZm9udC1zaXplPSIxMiIgZm9udC1mYW1pbHk9IlJvYm90bywgc2Fucy1zZXJpZiIgZmlsbD0iI0ZGRkZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgbGV0dGVyLXNwYWNpbmc9IjIiIGZvbnQtd2VpZ2h0PSI2MDAiIGZvbnQtc3R5bGU9Im5vcm1hbCIgdGV4dC1kZWNvcmF0aW9uPSJub25lIiBmaWxsLW9wYWNpdHk9IjEiIGZvbnQtdmFyaWFudD0ibm9ybWFsIiBzdHlsZT0idGV4dC10cmFuc2Zvcm06IHVwcGVyY2FzZTsiPlJFUVVJUkVTPC90ZXh0PjwhLS0tLT48dGV4dCBkYXRhLXYtODY1ODNhZmQ9IiIgeD0iMTM3LjA2NjY2OTQ2NDExMTMzIiB5PSIxNy41IiBkeT0iMC4zNWVtIiBmb250LXNpemU9IjEyIiBmb250LWZhbWlseT0iTW9udHNlcnJhdCwgc2Fucy1zZXJpZiIgZmlsbD0iI0ZGRkZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZm9udC13ZWlnaHQ9IjkwMCIgbGV0dGVyLXNwYWNpbmc9IjIiIGZvbnQtc3R5bGU9Im5vcm1hbCIgdGV4dC1kZWNvcmF0aW9uPSJub25lIiBmaWxsLW9wYWNpdHk9IjEiIGZvbnQtdmFyaWFudD0ibm9ybWFsIiBzdHlsZT0idGV4dC10cmFuc2Zvcm06IHVwcGVyY2FzZTsiPk5CVC1BUEk8L3RleHQ+PCEtLS0tPjwvc3ZnPg==)](https://modrinth.com/plugin/nbtapi)]

![GitHub Release](https://img.shields.io/github/v/release/xmoderlive/ViperX)
![GitHub License](https://img.shields.io/github/license/xmoderlive/ViperX)

ViperX is a Simple Minecraft Server Plugin for Clearing Player Inventory When Banned.

## Overview

ViperX helps server automatically clear certain NBT data when banned such as goods in the inventory obtained via unfair means or a custom NBT tag, handling of which is powered by `NBTAPI`.

## Features

- Clears player inventory data when a ban is applied
- Supports AdvancedBan and LiteBans as well as Vanilla ban system
- Removes custom NBT tags from player data
- Supports custom ban durations and duration presets
- Tracks pending clears
- Provides operator-only configuration menus

## Commands

| Command | Description |
| --- | --- |
| `/viperx` | Opens the main menu for operators; non-operators are sent to the public links menu |
| `/vx` | Alias for `/viperx` |

## Permissions

| Permission | Description | Default |
| --- | --- | --- |
| `viperx.menu.*` | Grants access to all ViperX menus | `op` |
| `viperx.mainMenu` | Opens the main menu | `op` |
| `viperx.settings` | Opens the settings menu | `op` |
| `viperx.pending` | Opens the pending clears menu | `op` |
| `viperx.links` | Opens the public links menu | `true` |
| `viperx.banMenu` | Opens the ban duration menu | `op` |

## Requirements

- Bukkit/Spigot/Paper server
- `NBTAPI`

## Installation

1. Download the latest ViperX release JAR.
2. Place it in your server's `plugins/` folder.
3. Install `NBTAPI` if it is not already present.
4. Start or restart the server.

## Configuration

After the first startup, ViperX generates its config files. The current default config includes offline-mode warnings, ban duration presets, playerdata paths, and NBT tags to clear.

```yaml
# False: Does not warn the currently logged-in admin if the server is in offline mode
# True: Warns the currently logged-in admin if the server is in offline mode
offline-mode-warning: true

global:
  ban-durations:
    # The amount of time the ban duration is allowed to be off by in milliseconds
    grace-period: 100
    # Exact durations that can be matched for inventory clearing
    duration:
      - "1s"
      - "1h"
      - "1d"
      - "1w"
      - "1y"
  # Relative paths to playerdata folders inside each world directory
  playerdata-path:
    - "playerdata"
    - "players/data"

  # NBT tags that are removed from player data
  NBT-Tags:
    - "Inventory"
    - "EnderItems"
```

## Changelog

See [`CHANGELOG.md`](CHANGELOG.md) for release notes and version history.

## Author

- [NotNightSky](https://www.github.com/NotNightSky) - Creator and maintainer of ViperX
