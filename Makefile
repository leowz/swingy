# **************************************************************************** #
#                                                                              #
#                                                         :::      ::::::::    #
#    Makefile                                           :+:      :+:    :+:    #
#                                                     +:+ +:+         +:+      #
#    By: zweng <zweng@student.42.fr>                +#+  +:+       +#+         #
#                                                 +#+#+#+#+#+   +#+            #
#    Created: 2022/08/19 12:41:50 by zweng             #+#    #+#              #
#    Updated: 2024/11/14 15:40:43 by zweng            ###   ########.fr        #
#                                                                              #
# **************************************************************************** #

# ----- varaibles -----

JC 			= javac
MV			= mvn
JRE 		= java
NAME 		= swingy.jar

# ----- Colors -----
BLACK		:="\033[1;30m"
RED			:="\033[1;31m"
GREEN		:="\033[1;32m"
PURPLE		:="\033[1;35m"
CYAN		:="\033[1;36m"
WHITE		:="\033[1;37m"
EOC			:="\033[0;0m"
#  # ==================

# ----- part rules -----
all:
	@$(MV) clean package

run:
	@$(JRE) -jar $(NAME) console

ui:
	@$(MV) clean package
	@$(JRE) -jar $(NAME)

build:
	@$(MV) package

clean: 
	@$(MV) clean
	@rm -rf map.json

re: fclean all

.PHONY: clean fclean re norme all
