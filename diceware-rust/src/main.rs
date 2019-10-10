
#[macro_use]
extern crate clap;

use clap::{Arg, App, AppSettings};
use derive_more::From;
use std::iter;

use diceware::*;

// #[non_exhaustive] // One day
#[derive(From, Debug)]
pub enum Error {
    // External Errors
    Io(std::io::Error),

    // My errors
    InvalidToken,

    // Allows you to add future errors without breaking compatibility
    // for your user's `match` arms.
    // This will eventually be done with #[non_exhaustive]
    #[doc(hidden)]
    __Nonexhaustive
}

pub type Result<T> = std::result::Result<T, Error>;

fn main() -> Result<()> {
    let matches = App::new(crate_name!())
        .version(crate_version!())
        .about(crate_description!())
        .setting(AppSettings::DisableVersion) // no --version flag
        .setting(AppSettings::UnifiedHelpMessage)
        .arg(Arg::with_name("join")
            .short("j")
            .long("join")
            .value_name("SEPARATOR")
            .help("Join words using separator [default: \\n]")
            .default_value("\n")
            .hide_default_value(true) // it doesn't print properly
            .takes_value(true))
        .arg(Arg::with_name("NUMBER")
            .required(false)
            .help("Sets the number of diceware words to select")
            .default_value("4")
            .index(1))
        .get_matches();


    let number = value_t!(matches.value_of("NUMBER"), u16).unwrap_or_else(|e| e.exit());

    let word_list = get_word_list();

    let word_stream = iter::repeat_with(|| get_diceware_word(&word_list));
    let mut words = word_stream.take(number as usize);

    let separator = matches.value_of("join").unwrap_or("\n");
    if let Some(word) = words.next() {
        print!("{}", word);
        for word in words {
            print!("{}", separator);
            print!("{}", word);
        }
        println!();
    }

    Ok(())
}

