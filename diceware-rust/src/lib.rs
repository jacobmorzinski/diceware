use rand::seq::SliceRandom;

extern crate phf;

// build.rs produces a codegen.rs file defining WORDLIST:
// static WORDLIST: phf::Map<&str,&str>
include!(concat!(env!("OUT_DIR"), "/codegen.rs"));

pub fn get_word(roll: &str) -> Option<String> {
    WORDLIST.get(roll).map(|s| s.to_string())
}

pub fn roll() -> String {
    let mut results = Vec::<&str>::with_capacity(5);
    let mut rng = rand::thread_rng();
    let choices = vec!["1", "2", "3", "4", "5", "6"];
    results.push(choices.choose(&mut rng).unwrap());
    results.push(choices.choose(&mut rng).unwrap());
    results.push(choices.choose(&mut rng).unwrap());
    results.push(choices.choose(&mut rng).unwrap());
    results.push(choices.choose(&mut rng).unwrap());
    let result = results.join("").to_string();
    result
}
