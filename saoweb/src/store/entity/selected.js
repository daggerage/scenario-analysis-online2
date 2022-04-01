class Selected {
  v = {}
  // {
  //    {id:'xxx', points:Set(0,1,...)},
  //    {},
  //    ...
  // }

  hasPoint(si, di) {
    return !!(this.v[si] && this.v[si]['points'].has(di))
  }

  addPoint(si, di, recordId) {
    if (!this.v[si]) {
      this.v[si] = {'id': recordId, 'points': new Set()}
    }
    this.v[si]['points'].add(di)
  }

  removePoint(si, di) {
    this.v[si]['points'].delete(di)
    if (this.v[si]['points'].length === 0) {
      delete this.v[si]
    }
  }

  clickPoint(si, di, recordId) {
    if (this.hasPoint(si, di)) {
      this.removePoint(si, di)
      return false
    } else {
      this.addPoint(si, di, recordId)
      return true
    }
  }

  getData() {
    return this.v
  }
}

export default Selected
