#! /bin/bash

# get commit msg
if [[ $CI_COMMIT_MESSAGE ]]; then
    msg=$CI_COMMIT_MESSAGE
else
    read msg < .git/COMMIT_EDITMSG
fi

echo -e "\033[33m The commit msg: \033[0m $msg"

# if the msg is merge then skip it
mergePattern='^Merge '
if [[ $msg =~ $mergePattern ]]; then
    echo -e "\033[32m skip the merge, commit success! \033[0m"
    exit 0
fi

# check the commit msg
maxLength=50
length=${#msg}
pattern='^(feat|fix|hotfix|test|refactor|docs|style|chore)\(.*\):.*$'

if [[ $msg =~ $pattern ]]; then
    if [[ $length -gt $maxLength ]]; then
        echo -e "\033[31m Error: the msg over max length \033[m"
        exit 1
    fi
    echo -e "\033[32m commit success! \033[0m"
else
    echo -e "\033[31m Error: the commit message is irregular \033[m"
	echo -e "\033[31m Error: type must be one of [feat,fix,hotfix,docs,style,refactor,test,chore] \033[m"
    echo -e "\033[31m eg: feat(user): add the user login \033[m"
    exit 1
fi
