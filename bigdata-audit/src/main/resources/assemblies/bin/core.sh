#!/usr/bin/env bash
BASE="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"

################################
# params
################################
#parse comand name, app name, app parameter
OPERATE=$1
shift
APP_PARAMS=$@

################################
# constants
################################
#opts
APP_OPTS="`cat $BASE/conf/jvm.options|grep -v -e ^#|grep -v -e ^$|awk '{{printf"%s ",$0}}'`"

APP_MAIN="bigdata.audit.AuditApp"
APP_TMPDIR="/tmp"
#dir
JAVA="${JAVA_HOME}/bin/java"
APP_CONF="$BASE/conf"
APP_LOGS="$BASE/logs"
APP_PIDS="$BASE/pids"

APP_NAME="bigdata-audit"
APP_PID="${APP_PIDS}/${APP_NAME}.pid"
APP_LIB="$BASE/lib"
APP_JAR="`ls ${APP_LIB}/${APP_NAME}-*.jar`"
APP_CP="${APP_CONF}:${APP_JAR}:${APP_LIB}/*"

#file
APP_OUT="${APP_LOGS}/${APP_NAME}.out"

#port
APP_PORT="`cat ${APP_CONF}/audit.conf |grep api.port|cut -d '=' -f 2|cut -d ' ' -f 2`"


################################
# functions
################################
# Shell Colors
GREEN=$'\e[0;32m'
LGREEN=$'\e[1;32m'
RED=$'\e[0;31m'
LRED=$'\e[1;31m'
BLUE=$'\e[0;34m'
LBLUE=$'\e[1;34m'
RESET=$'\e[m'
function error() {
    debug error $@
}
function debug() {
    if [[ "$1" == "warn" ]]; then
        shift
        echo -e "     ${LBLUE}$1${RESET}"
    elif [[ "$1" == "info" ]]; then
        shift
        echo -e "     ${BLUE}$1${RESET}"
    elif [[ "$1" == "error" ]]; then
        shift
        echo -e "     ${RED}ERROR:${LRED} $@${RESET}"
    	exit 1
    else
        echo -e $@
    fi
}
function cmd() {
    echo -e  "CMD: $OPERATE \t ${APP_PARAMS}"
}

function env() {

	cat << EOF
ENV:
	APP_CONF: ${APP_CONF}
	APP_LOGS: ${APP_LOGS}
	APP_OUT : ${APP_OUT}
	APP_LIB : ${APP_LIB}
	APP_PORT: ${APP_PORT}
EOF
}

function init() {
	mkdir -p ${APP_CONF} ${APP_LOGS} ${APP_PIDS}
}

function check() {
    if [ -f ${APP_PID} ]; then
        PID=`getPid`
        if kill -0 ${PID} > /dev/null 2>&1; then
            debug error "The ${APP_NAME} already started! PID: ${PID}"
            exit 1
        fi
    fi
}

function start() {
	check
	cd ${BASE}
	${JAVA} ${APP_OPTS} -cp ${APP_CP} ${APP_MAIN} ${APP_PARAMS} > ${APP_OUT} 2>&1 &
    PID=$!
    echo ${PID} > "${APP_PID}"
    debug info "${APP_NAME}(pid ${PID}) is started."
}

function status() {
    PID=`getPid`
    if [ -n "$PID" ]; then
        debug info "${APP_NAME}(pid ${PID}) is running..."
    else
        debug info "${APP_NAME} is not running."
    fi
}

function getPid() {
#    if [ -f ${APP_PID} ]; then
#        return "`cat ${APP_PID}`"
#    else
        echo "` ps aux|grep java|grep ${APP_MAIN}|awk '{print $2}'`"
#    fi
}

function stop() {
    PID=`getPid`
    if kill -0 ${PID} > /dev/null 2>&1; then
        debug info "stopping ${APP_NAME} PID: ${PID}"
        kill ${PID}
    else
        debug error "${APP_NAME} is not running."
    fi
}

################################
# run
################################
init


cmd
env
echo "RES:"

case  $OPERATE in
    start)
		start
	;;
    stop)
		stop
	;;
	status)
        status
    ;;
esac


