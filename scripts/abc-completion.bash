#!/bin/bash
# https://iridakos.com/programming/2018/03/01/bash-programmable-completion-tutorial

_doabc_completions()
{
    # Disable default. This works only for Bash 3+?
    compopt +o bashdefault +o default

    if [ "${#COMP_WORDS[@]}" != "2" ]; then
        # Remove abc from the list and pass it to abc. This will return a superset of the options
        # that we need to filter using COMP_WORDS
        if [ "$(abc __private_autocomplete "${COMP_WORDS[@]:1}")" == "requires_one_file" ]; then
            COMPREPLY=()
            # Enable default autocompletion using files and directories
            compopt -o bashdefault -o default
        fi
    else
        # Load the available commands from the "help" command of the script.
        # COMPREPLY is the default name   
        ABC_COMMANDS=$(abc help 2> /dev/null | tr "\n" " ")
        COMPREPLY=($(compgen -W "${ABC_COMMANDS}" "${COMP_WORDS[1]}"))
    fi
}

# -o default in combination with COMPREPLY=() interprets the need of using a file/directory
complete -F _doabc_completions abc