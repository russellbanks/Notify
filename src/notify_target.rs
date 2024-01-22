use poise::ChoiceParameter;

#[derive(ChoiceParameter, Eq, PartialEq)]
pub enum NotifyTarget {
    Here,
    Everyone,
}

impl NotifyTarget {
    pub const fn mention(&self) -> &'static str {
        match self {
            Self::Here => "@here",
            Self::Everyone => "@everyone",
        }
    }
}
