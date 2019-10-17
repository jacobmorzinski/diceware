#[macro_use]
extern crate clap;

use clap::{App, AppSettings, Arg};
use derive_more::From;
use std::iter;

use crate::Error::NoWordForRoll;
use diceware::*;

// #[non_exhaustive] // One day
#[derive(From, Debug)]
pub enum Error {
    // External Errors
    Io(std::io::Error),

    // My errors
    NoWordForRoll {
        roll: String,
    },

    // Allows you to add future errors without breaking compatibility
    // for your user's `match` arms.
    // This will eventually be done with #[non_exhaustive]
    #[doc(hidden)]
    __Nonexhaustive,
}

pub type Result<T> = std::result::Result<T, Error>;

fn main() -> Result<()> {
    let matches = App::new(crate_name!())
        .version(crate_version!())
        .about(crate_description!())
        .setting(AppSettings::DisableVersion) // no --version flag
        .setting(AppSettings::UnifiedHelpMessage)
        .arg(
            Arg::with_name("SEPARATOR")
                .short("j")
                .long("join")
                .takes_value(true)
                .default_value("\n")
                .hide_default_value(true) // it doesn't print properly
                .help("Join words using separator [default: \\n]"),
        )
        .arg(
            Arg::with_name("NUMBER")
                .required(false)
                .default_value("4")
                .help("Sets the number of diceware words to select"),
        )
        .get_matches();

    let separator = matches.value_of("SEPARATOR").unwrap_or("\n");
    let number = value_t!(matches.value_of("NUMBER"), u16).unwrap_or_else(|e| e.exit());

    println!("[Iterator style]");

    let word_stream = iter::repeat_with(|| get_word());
    let mut words = word_stream.take(number as usize);

    if let Some(word) = words.next() {
        print!("{}", word);
        for word in words {
            print!("{}", separator);
            print!("{}", word);
        }
        println!();
    }

    println!("[Explicit roll style]");

    let mut words = Vec::<String>::new();

    for _ in 0..number {
        let diceroll = roll();
        match get_word_by_roll(&diceroll) {
            Some(word) => words.push(word),
            None => return Err(NoWordForRoll { roll: diceroll }),
        }
    }

    print!("{}", words.join(separator));
    println!();

    Ok(())
}
