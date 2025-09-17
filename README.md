
# ViperX

[![GitHub](https://forthebadge.com/images/featured/featured-gluten-free.svg)](https://github.com/xmoderlive/viperx) [![forthebadge](https://forthebadge.com/images/featured/featured-built-with-love.svg)](https://github.com/xmoderlive/viperx)

**Just a simple minecraft plugin that clears any player's inventory when banned and is fully compatible with AdvancedBan and LiteBan.**

![GitHub Release](https://img.shields.io/github/v/release/xmoderlive/ViperX)
![GitHub License](https://img.shields.io/github/license/xmoderlive/ViperX)

## Features

- Clear inventory on ban
- Support for advancedBans
- Support for liteBans
- Clear inventory on custom duration
- Config menu
- Pending clear list (add/remove)
- Duration list (add/remove)

<details>
<summary>config.yaml</summary>

```
# False:Does not warn the currently logged-in admin, If the server is in offline mode
# True:Warns the currently logged-in admin, If the server is in offline mode
offline-mode-warning: true

global:
  ban-durations:
    grace-period: 100
    duration:
    - 1y
    - 1m
    - 1mo

```
</details>




## Authors

- [xmoderlive](https://www.github.com/xmoderlive)
