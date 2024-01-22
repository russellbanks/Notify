#[derive(Eq, PartialEq)]
pub enum Action {
    Join,
    Leave,
    Switch,
    Stream,
    Video,
}

impl Action {
    pub const fn as_phrase(&self) -> &'static str {
        match self {
            Self::Join => "joined",
            Self::Leave => "left",
            Self::Switch => "switched to",
            Self::Stream => "is live in",
            Self::Video => "turned their video on in",
        }
    }

    pub const fn as_emoji(&self) -> char {
        match self {
            Self::Join => '🎧',
            Self::Leave => '🚪',
            Self::Switch => '🔁',
            Self::Stream => '🔴',
            Self::Video => '📷',
        }
    }
}
