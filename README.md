<br/>
<p align="center">
  <a href="https://github.com/BanDev/Notify">
    <img src="assets/logo-round.svg" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Notify</h3>

  <p align="center">
    A voice channel notification bot for Discord
    <br/>
    <br/>
    <a href="https://github.com/BanDev/Notify/issues">Report Bug</a>
    .
    <a href="https://github.com/BanDev/Notify/issues">Request Feature</a>
  </p>
</p>


![Contributors](https://img.shields.io/github/contributors/BanDev/Notify?color=dark-green) ![Issues](https://img.shields.io/github/issues/BanDev/Notify) ![License](https://img.shields.io/github/license/BanDev/Notify)

## Table Of Contents

* [About the Project](#about-the-project)
* [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)

## About The Project

Notify is a voice channel notification bot for Discord. It is a self-hosted Discord bot, written in TypeScript, that can send a message to a Discord  chat with any updates to a voice channel. For example, sending a notification when a user has joined a voice channel is ideal for small communities and friends. This functionality extends to wider usage, such as whenever a user goes live or screen shares. This project was originally created so that our group of friends can all join the voice channel when they know that someone has joined.

## Built With

* [Discord.js 13](https://github.com/discordjs/discord.js)
* [TypeScript](https://github.com/microsoft/TypeScript)

## Getting Started

This project is stable as of [v1.0.0](https://github.com/BanDev/Notify/releases/tag/v1.0.0). However, this is an actively maintained and worked on project. You can clone into the repository and run the bot locally, or you can deploy to Heroku using the button under the installation section below. Deploying to Heroku is our recommended way of hosting the bot, however you are free to use any means of hosting.

### Prerequisites

To build Notify, you will need [Node.js](https://nodejs.org/) as this project is written in TypeScript. We use the latest version of Node.js to build this project which is currently Node.js 17. If you would like to contribute or edit the code yourself, you should install a text editor or an IDE for JavaScript/TypeScript of your choice. For example, [Visual Studio Code](https://github.com/Microsoft/vscode) or [Atom](https://github.com/atom/atom). These prerequisites are only necessary if you are hosting or working on this bot locally. Hosting this bot on Heroku does not require this.

1. Install [Git](https://git-scm.com/downloads)

2. Install [Node.js](https://nodejs.org/en/download/)

### Installation

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/bandev/notify)

or

1. Clone the repo

```sh
git clone https://github.com/BanDev/Notify.git
```

2. Navigate into the directory

```sh
cd Notify
```

3. Install dependencies

```sh
npm install
```

4. Build the project

```sh
npm run build
```

## Usage

Once Notify has been installed and is running, you must create a voice channel called 'vcupdates'. Once this has been done, Notify is up and running and will start sending messages in that channel with updates on voice channels. The functionality to choose the text channel that these messages get sent into is marked on the [Roadmap](#roadmap).

## Roadmap

As this project is written from scratch in Jetpack Compose, not everything has been added from the original [Buddha Quotes](https://github.com/BanDev/Buddha-Quotes).

- [x] Notify when a user has joined a voice channel
- [x] Notify when a user has left a voice channel
- [x] Notify when user has switched voice channel
- [x] Notify when user goes live in a voice channel
- [ ] Server level configuration (enabling/disabling which notifications should send and the channel that it should send to)

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.
* If you have suggestions for adding or removing projects, feel free to [open an issue](https://github.com/BanDev/Buddha-Quotes-Compose/issues/new) to discuss it, or directly create a pull request after you edit the *README.md* file with necessary changes.
* Please make sure you check your spelling and grammar.
* Create individual PR for each suggestion.
* Please also read through the [Code Of Conduct](https://github.com/BanDev/Buddha-Quotes-Compose/blob/main/CODE_OF_CONDUCT.md) before posting your first idea as well.

### Creating A Pull Request

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
[![GNU GPLv3 Logo](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

Notify is Free Software: You can use, study share and improve it at your will. Specifically you can redistribute and/or modify it under the terms of the [GNU General Public License](http://www.gnu.org/licenses/gpl-3.0.en.html) as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

## Authors

* **Russell Banks** - *Comp Sci Student* - [Russell Banks](https://github.com/russellbanks/)
* **Jack Devey** - *Comp Sci Student* - [Jack Devey](https://github.com/jackdevey/)
