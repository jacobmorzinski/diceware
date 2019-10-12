use rand::seq::SliceRandom;

extern crate phf;

// build.rs produces a codegen.rs file defining WORDLIST:
// static WORDLIST: phf::Map<K,V>
include!(concat!(env!("OUT_DIR"), "/codegen.rs"));

pub fn get_word(roll: &str) -> Option<&str> {
    WORDLIST.get(roll).cloned()
}

pub fn roll() -> String {
    let mut rng = rand::thread_rng();
    let choices = vec!["1", "2", "3", "4", "5", "6"];
    let result = choices.choose(&mut rng).unwrap().to_string();
    result
}

pub fn roll5() -> String {
    let mut rng = rand::thread_rng();
    let choices = vec!["1", "2", "3", "4", "5", "6"];
    let result1 = choices.choose(&mut rng).unwrap();
    let result2 = choices.choose(&mut rng).unwrap();
    let result3 = choices.choose(&mut rng).unwrap();
    let result4 = choices.choose(&mut rng).unwrap();
    let result5 = choices.choose(&mut rng).unwrap();
    let result = format!("{}{}{}{}{}", result1, result2, result3, result4, result5);
    result
}
