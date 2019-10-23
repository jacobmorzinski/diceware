use std::iter;
use structopt::{clap::AppSettings, StructOpt};

#[derive(Debug, StructOpt)]
#[structopt(about, setting(AppSettings::UnifiedHelpMessage))]
struct Opt {
    #[structopt(
        short = "j",
        long = "join",
        default_value = "\n",
        hide_default_value = true, // it doesn't print properly
        help = "Join words using separator [default: \\n]"
    )]
    separator: String,

    #[structopt(
        required = false,
        default_value = "4",
        help = "Sets the number of diceware words to select"
    )]
    number: usize,

    #[structopt(
        long,
        hidden = true,
        required = false,
        possible_values = &["stream", "roll", "simple"],
        default_value = "stream",
        help = "Internal"
    )]
    method: String,
}

pub type Result<T> = std::result::Result<T, diceware::Error>;

fn main() -> Result<()> {
    let opt = Opt::from_args();
    let separator: &str = &opt.separator;
    let number: usize = opt.number;
    let method: &str = &opt.method;

    print!("Test roll: ");
    let test_roll = diceware::Roll5::new();
    println!("{:?}", &test_roll);

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
