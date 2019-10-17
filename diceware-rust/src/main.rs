#[macro_use]
extern crate clap;

use clap::{App, AppSettings, Arg};
use std::iter;

use diceware;

pub type Result<T> = std::result::Result<T, diceware::Error>;

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
            Arg::with_name("METHOD")
                .short("m")
                .long("method")
                .hidden(true)
                .required(false)
                .possible_values(&["stream", "roll", "simple"])
                .default_value("stream")
                .help("Internal"),
        )
        .arg(
            Arg::with_name("NUMBER")
                .required(false)
                .default_value("4")
                .help("Sets the number of diceware words to select"),
        )
        .get_matches();

    let separator = matches.value_of("SEPARATOR").unwrap();
    let number = value_t!(matches.value_of("NUMBER"), usize).unwrap_or_else(|e| e.exit());
    let method = matches.value_of("METHOD").unwrap();

    if method == "stream" {
        let word_stream = iter::repeat_with(|| diceware::get_word());
        let mut words = word_stream.take(number);
        if let Some(word) = words.next() {
            print!("{}", word);
            for word in words {
                print!("{}", separator);
                print!("{}", word);
            }
            println!();
        }
    } else if method == "roll" {
        let mut words = Vec::<String>::with_capacity(number);
        for _ in 0..number {
            let diceroll = diceware::Roll::new();
            words.push(diceware::get_word_by_roll(&diceroll));
        }
        print!("{}", words.join(separator));
        println!();
    } else if method == "simple" {
        if number > 0 {
            let diceroll = diceware::roll();
            let word = diceware::get_word_by_str(&diceroll)?;
            print!("{}", word);
            for _ in 1..number {
                let diceroll = diceware::roll();
                let word = diceware::get_word_by_str(&diceroll)?;
                print!("{}", separator);
                print!("{}", word);
            }
        }
        println!();
    } else {
        panic!("unimplemented method");
    }

    Ok(())
}
