if [ "$color_prompt" = yes ]; then
    PS1='\[\033[1;37m\]\[\033[41m\]${debian_chroot:+($debian_chroot)}\u@\h:\w\$\[\033[00m\] '
else
    PS1='${debian_chroot:+($debian_chroot)}\u@\h:\w\$ '
fi
unset color_prompt force_color_prompt
