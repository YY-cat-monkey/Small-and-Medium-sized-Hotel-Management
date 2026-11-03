function ajaxGet(url, success) {
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function(res) {
            if (res.code === 200) {
                if (success) success(res.data);
            } else {
                alert(res.msg || '操作失败');
            }
        },
        error: function(xhr) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                alert('请求失败，请稍后重试');
            }
        }
    });
}

function ajaxPost(url, data, success) {
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(res) {
            if (res.code === 200) {
                if (success) success(res.data);
            } else {
                alert(res.msg || '操作失败');
            }
        },
        error: function(xhr) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                alert('请求失败，请稍后重试');
            }
        }
    });
}

function ajaxPostForm(url, data, success) {
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        dataType: 'json',
        success: function(res) {
            if (res.code === 200) {
                if (success) success(res.data);
            } else {
                alert(res.msg || '操作失败');
            }
        },
        error: function(xhr) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                alert('请求失败，请稍后重试');
            }
        }
    });
}

function ajaxDelete(url, success) {
    $.ajax({
        url: url,
        type: 'DELETE',
        dataType: 'json',
        success: function(res) {
            if (res.code === 200) {
                if (success) success(res.data);
            } else {
                alert(res.msg || '操作失败');
            }
        },
        error: function(xhr) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                alert('请求失败，请稍后重试');
            }
        }
    });
}

function formatDate(date) {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const day = d.getDate().toString().padStart(2, '0');
    const hour = d.getHours().toString().padStart(2, '0');
    const minute = d.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day} ${hour}:${minute}`;
}

function formatDateOnly(date) {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const day = d.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function roomStatusText(status) {
    const map = {0: '空闲', 1: '预订', 2: '在用', 3: '清洁', 4: '维修'};
    return map[status] || '未知';
}

function roomStatusClass(status) {
    const map = {0: 'badge-free', 1: 'badge-booked', 2: 'badge-inuse', 3: 'badge-cleaning', 4: 'badge-repair'};
    return map[status] || 'badge-secondary';
}

function bookingStatusText(status) {
    const map = {0: '待确认', 1: '已确认', 2: '已入住', 3: '已取消'};
    return map[status] || '未知';
}

function billStatusText(status) {
    const map = {0: '未结', 1: '已结'};
    return map[status] || '未知';
}
